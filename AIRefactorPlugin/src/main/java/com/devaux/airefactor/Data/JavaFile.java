package com.devaux.airefactor.Data;

public class JavaFile {
    private String relativePath;
    private String content;

    public JavaFile(String relativePath, String content) {
        setRelativePath(relativePath);
        setContent(content);
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
