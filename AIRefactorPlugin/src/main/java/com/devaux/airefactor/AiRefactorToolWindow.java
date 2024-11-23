package com.devaux.airefactor;

import com.devaux.airefactor.Data.JavaFile;
import com.devaux.airefactor.Data.RefactorSuggestion;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
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
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;
import com.intellij.openapi.diagnostic.Logger;

public class AiRefactorToolWindow implements ToolWindowFactory {
    private static final Logger LOG = Logger.getInstance(AiRefactorToolWindow.class);

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        RefactorToolWindowContent toolWindowContent = new RefactorToolWindowContent(toolWindow);
        Content content = ContentFactory.getInstance().createContent(toolWindowContent.getContentPanel(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

    private static class RefactorToolWindowContent {
        private final JPanel contentPanel = new JPanel();

        public RefactorToolWindowContent(ToolWindow toolWindow) {
            contentPanel.setLayout(new BorderLayout(0, 20));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
            contentPanel.add(createControlsPanel(), BorderLayout.NORTH);
            contentPanel.add(createAnalysisReportPanel(), BorderLayout.CENTER);
        }

        @NotNull
        private JPanel createControlsPanel() {
            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new BorderLayout(10,10));
            controlPanel.add(new JLabel("Project refactor analysis."), BorderLayout.NORTH);
            controlPanel.add(new JButton("Run refactor analysis"), BorderLayout.CENTER);
            return controlPanel;
        }

        @NotNull
        private JPanel createAnalysisReportPanel() {
            JPanel analysisPanel = new JPanel();
            analysisPanel.add(new JLabel("<Add analysis here>"), BorderLayout.CENTER);
            return analysisPanel;
        }

        public JPanel getContentPanel() {
            return contentPanel;
        }

        private ArrayList<JavaFile> getProjectJavaFiles(Project project) throws IOException {
            ArrayList<JavaFile> javaFiles = new ArrayList<>();
            Collection<VirtualFile> virtualJavaFiles =  FileTypeIndex.getFiles(JavaFileType.INSTANCE, GlobalSearchScope.projectScope(project));
            for(VirtualFile f : virtualJavaFiles) {
                String path = f.getPath();
                String content = VfsUtilCore.loadText(f);
                javaFiles.add(new JavaFile(path, content));
            }
            return javaFiles;
        }

        private Project getCurrentOpenProject() {
            Project[] openProjects = ProjectManager.getInstance().getOpenProjects();

            if (openProjects.length > 0) {
                // Return the first open project (most commonly used in single-project instances)
                return openProjects[0];
            }
            return null;
        }

        private ArrayList<RefactorSuggestion> RequestAnalysis() throws IOException, InterruptedException {
            ArrayList<RefactorSuggestion> suggestions = new ArrayList<RefactorSuggestion>();;
            Project project = getCurrentOpenProject();

            if(project != null) {
                ArrayList<JavaFile> javaFiles = getProjectJavaFiles(project);

                Gson gson = new Gson();
                String json = gson.toJson(javaFiles);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://127.0.0.1/api/endpoint"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                try (HttpClient client = HttpClient.newHttpClient()) {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    if(response.statusCode() == 200) {
                        String jsonResponse = response.body();

                        Type listType = new TypeToken<ArrayList<RefactorSuggestion>>(){}.getType();
                        suggestions = gson.fromJson(jsonResponse, listType);
                    } else {
                        LOG.error("HTTP Post request NOK: " + response.statusCode());
                    }
                }
            }

            return suggestions;
        }
    }
}
