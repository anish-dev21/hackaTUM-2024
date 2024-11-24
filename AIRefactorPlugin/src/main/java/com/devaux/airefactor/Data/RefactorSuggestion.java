package com.devaux.airefactor.Data;

public class RefactorSuggestion {
    private String className;
    private String type;
    private String packageName;
    private String complexity;
    private String category;
    private String priority;
    private String issue;
    private String suggestion;
    private String impact;

    // Getters and Setters
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getComplexity() {
        return complexity;
    }

    public void setComplexity(String complexity) {
        this.complexity = complexity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    // toString Method for Debugging
    @Override
    public String toString() {
        return "RefactorSuggestion{" +
                "className='" + className + '\'' +
                ", type='" + type + '\'' +
                ", packageName='" + packageName + '\'' +
                ", complexity='" + complexity + '\'' +
                ", category='" + category + '\'' +
                ", priority='" + priority + '\'' +
                ", issue='" + issue + '\'' +
                ", suggestion='" + suggestion + '\'' +
                ", impact='" + impact + '\'' +
                '}';
    }
}
