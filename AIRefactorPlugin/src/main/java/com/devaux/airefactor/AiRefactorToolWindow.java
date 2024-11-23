package com.devaux.airefactor;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class AiRefactorToolWindow implements ToolWindowFactory {
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
            contentPanel.add(createControlsPanel(), BorderLayout.CENTER);
        }

        @NotNull
        private JPanel createControlsPanel() {
            JPanel controlPanel = new JPanel();
            controlPanel.add(new JLabel("HELLO!"));
            return controlPanel;
        }

        public JPanel getContentPanel()
        {
            return contentPanel;
        }
    }
}
