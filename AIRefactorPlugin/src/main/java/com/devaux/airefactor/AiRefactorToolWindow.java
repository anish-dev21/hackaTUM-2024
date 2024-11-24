package com.devaux.airefactor;

import com.devaux.airefactor.Data.JavaFile;
import com.devaux.airefactor.Data.RefactorSuggestion;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import com.intellij.openapi.diagnostic.Logger;

public class AiRefactorToolWindow implements ToolWindowFactory {
    private static final Logger LOG = Logger.getInstance(AiRefactorToolWindow.class);
    private JTextArea recommendationTextArea = new JTextArea();


    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        JPanel toolWindowContent = createMinimalisticToolWindowPanel();
        Content content = ContentFactory.getInstance().createContent(toolWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    @NotNull
    private JPanel createMinimalisticToolWindowPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(new Color(30, 30, 30)); // Darker gray background

        // Add logo
        JLabel logoLabel;
        URL logoUrl = getClass().getResource("/images/logo.png"); // Update to match your logo's path in 'resources'
        if (logoUrl != null) {
            ImageIcon icon = new ImageIcon(logoUrl);
            Image image = icon.getImage(); // Get the original image
            Image scaledImage = image.getScaledInstance(300, 200, Image.SCALE_SMOOTH); // Scale the image
            logoLabel = new JLabel(new ImageIcon(scaledImage)); // Create a new ImageIcon with the scaled image
        } else {
            LOG.error("Logo resource not found at /images/logo.png");
            logoLabel = new JLabel("Logo Missing");
        }

        // Set alignment and add to the panel
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(logoLabel, BorderLayout.NORTH);

        // Add "Analyze Code" button
        JButton analyzeButton = new JButton("Analyze Code");
        analyzeButton.setBackground(new Color(63, 81, 181));
        analyzeButton.setForeground(Color.WHITE);
        analyzeButton.setFocusPainted(false);
        analyzeButton.setFont(new Font("Sans-Serif", Font.BOLD, 14));
        analyzeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        analyzeButton.addActionListener(e -> {
            try {
                LOG.info("Running request analysis...");
                Objects.requireNonNull(runRefactorAnalysis()).thenAccept(refactorSuggestions -> {
                    updateRecommendationsPanel(contentPanel, refactorSuggestions);
                });
//                updateRecommendationsPanel(contentPanel, suggestions);
            } catch (Exception ex) {
                LOG.error("Error performing refactor analysis: " + ex.getMessage(), ex);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(30, 30, 30)); // Match the darker background
        buttonPanel.add(analyzeButton);
        contentPanel.add(buttonPanel, BorderLayout.NORTH);

        // Add recommendations panel
//        JPanel recommendationsPanel = new JPanel();
//        recommendationsPanel.setLayout(new BoxLayout(recommendationsPanel, BoxLayout.Y_AXIS));
//        recommendationsPanel.setBackground(new Color(30, 30, 30)); // Match the darker background
//        JScrollPane scrollPane = new JScrollPane(recommendationsPanel);
//        scrollPane.setBorder(BorderFactory.createEmptyBorder());
//        contentPanel.add(scrollPane, BorderLayout.SOUTH);

        JPanel textArea = createScrollableTextField();
        contentPanel.add(textArea, BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel createScrollableTextField() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        recommendationTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(recommendationTextArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void updateRecommendationsPanel(JPanel contentPanel, ArrayList<RefactorSuggestion> suggestions) {
        String recommendations = "";
        for(RefactorSuggestion suggestion : suggestions) {
            recommendations += "================================================================\n";
            recommendations += "Class: " + suggestion.getClassName() + "\n";
            recommendations += "Complexity: " + suggestion.getComplexity() + "\n";
            recommendations += "Priority: " + suggestion.getPriority() + "\n";
            recommendations += "Issue: " + suggestion.getIssue() + "\n";
            recommendations += "Suggestion: " + suggestion.getSuggestion() + "\n";
            recommendations += "Impact: " + suggestion.getImpact() + "\n";
        }

        recommendationTextArea.setText(recommendations);
    }

    private CompletableFuture<ArrayList<RefactorSuggestion>> runRefactorAnalysis() throws IOException, InterruptedException {
        Project currentProject = getCurrentOpenProject();
        if (currentProject == null) {
            LOG.warn("No open project found.");
            return null;
        }

        ArrayList<JavaFile> javaFiles = getJavaFilesFromProject(currentProject);

        Gson gson = new Gson();
        String requestBody = gson.toJson(new ProjectFilesRequest(javaFiles));
        LOG.info("Sending POST request with body: \n" + requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:5000/process_java_files"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    String responseBody = response.body();
                    LOG.warn("HttpResponse:" + responseBody);
                    try {
                        // Define the Type for the list of RefactorSuggestion
                        Type listType = new TypeToken<ArrayList<RefactorSuggestion>>() {}.getType();

                        // Parse the JSON response into a list of RefactorSuggestion
                        return gson.fromJson(responseBody, listType);
                    } catch (Exception e) {
                        LOG.error("Failed to parse JSON response", e);
                        return new ArrayList<RefactorSuggestion>(); // Return an empty list on error
                    }
                })
                .exceptionally(e -> {
                    LOG.error("Error during async HTTP request", e);
                    return new ArrayList<>(); // Return an empty list on error
                });
    }

    private ArrayList<JavaFile> getJavaFilesFromProject(Project project) throws IOException {
        ArrayList<JavaFile> javaFiles = new ArrayList<>();
        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(JavaFileType.INSTANCE, GlobalSearchScope.projectScope(project));

        for (VirtualFile file : virtualFiles) {
            String path = file.getPath();
            String content = VfsUtilCore.loadText(file);
            javaFiles.add(new JavaFile(path, content));
        }

        return javaFiles;
    }

    private Project getCurrentOpenProject() {
        Project[] openProjects = ProjectManager.getInstance().getOpenProjects();
        return (openProjects.length > 0) ? openProjects[0] : null;
    }

    // Utility class for JSON request structure
    private static class ProjectFilesRequest {
        private final ArrayList<JavaFile> files;

        public ProjectFilesRequest(ArrayList<JavaFile> files) {
            this.files = files;
        }

        public ArrayList<JavaFile> getFiles() {
            return files;
        }
    }
}