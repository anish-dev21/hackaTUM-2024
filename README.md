# intelliJent (hackaTUM-2024)

Welcome to the **intelliJent (hackaTUM-2024)** repository! This is a hackathon project for JetBrains that combines a robust backend API with an innovative AI-powered plugin for code refactoring, aimed at simplifying workflows and enhancing developer productivity. It is designed for use in modern software development environments, offering features like intelligent code suggestions and scalable backend services.

---

## Table of Contents

1. [Repository Structure](#repository-structure)
2. [Branches Overview](#branches-overview)
3. [Getting Started](#getting-started)
4. [Usage Instructions](#usage-instructions)
5. [Contributing](#contributors)

---

## Repository Structure

### 1. Backend Services
The backend is implemented in Python and includes the following files:

- **`API_Backend.py`**: A core backend script handling API requests and responses.
- **`backend.py`**: Backend logic implementation.
- **`run_back.py`**: Script to initialize and start backend services.
- **`swarm_script.py`**: Potentially used for container orchestration or swarm management.
- **`test_requests.py`**: A utility script for testing the backend APIs.
- **`input.json`**: A sample input data file for configuration or testing.
- **`api_debug.log`**: Debugging log file for tracking API-related errors and performance.

---

### 2. AI Refactor Plugin
Located in the `AIRefactorPlugin` directory, this component is a Java-based plugin designed for IntelliJ IDEA. It leverages AI to provide suggestions for refactoring Java code and includes:

- **Build and Configuration**:
  - `build.gradle.kts` and `settings.gradle.kts`: Gradle files for project configuration.
  - Gradle wrapper files (`gradlew`, `gradlew.bat`) and related settings.

- **Source Code**:
  - **Java Files**:
    - `AiRefactorToolWindow.java`: Core implementation for the plugin's user interface.
    - `JavaFile.java` and `RefactorSuggestion.java`: Representations of Java files and AI suggestions.

  - **Resources**:
    - `META-INF/plugin.xml`: Plugin configuration for IntelliJ IDEA.
    - Icons and images for branding (`pluginIcon.svg`, `logo.png`).

---

### 3. Test Code
The `test_codes` directory contains Java test programs that can be used to evaluate the plugin or backend functionality:
- `calculator.java`
- `main.java`
- `person.java`
- `util.java`

---

### 3. Contributors
- Muhammad Aman Ahmad Tifli
- Joaquín Marí Marcos
- Anish Biswas


## Branches Overview

### `main`
The central branch of the repository, containing stable code for the backend, AI plugin, and test files.

### `plugin-development`
Focused on improving the AI Refactor Plugin, this branch includes:
- Experimental refactoring features.
- User interface enhancements.

### `backend-improvements`
Dedicated to refining backend services:
- Optimizing API performance.
- Adding new endpoints and features.

### `testing`
A specialized branch for testing the application, covering:
- Plugin functionality with test Java files.
- API stability and integration tests.

---

---

## 🚀 Getting Started

### Prerequisites
Ensure you have the following installed:
- **Python 3.10+**: For backend development.
- **Java JDK 17+**: For plugin development.
- **IntelliJ IDEA**: For working with the AI Refactor Plugin.

---

### Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/anish-dev21/hackaTUM-2024.git
   ```
2. **Install Backend Dependencies**:
   ```bash
   pip install -r requirements.txt
   ```
3. **Set Up the Plugin**:
   - Open the `AIRefactorPlugin` directory in IntelliJ IDEA.
   - Sync the Gradle project to resolve dependencies.

---

## 🛠️ Usage Instructions

### Running the Backend
Start the backend by running:
```bash
python run_back.py
```

### Using the AI Refactor Plugin
1. Open the `AIRefactorPlugin` in IntelliJ IDEA.
2. Build and run the plugin.
3. Test its functionality using sample Java files in the `test_codes` directory.

### Testing Backend APIs
Use the `test_requests.py` script to validate backend APIs:
```bash
python test_requests.py
```
---

## 🤝 Contributing

Contributions are welcome! Follow these steps to contribute:

1. **Fork the Repository**:
   ```bash
   git fork https://github.com/anish-dev21/hackaTUM-2024.git
   ```
2. **Create a Feature Branch**:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. **Commit Changes**:
   ```bash
   git commit -m "Add your feature description"
   ```
4. **Push to GitHub**:
   ```bash
   git push origin feature/your-feature-name
   ```
5. **Open a Pull Request**: Compare your branch with `main` and submit the PR.


## 📞 Contact

For queries, suggestions, or collaboration, feel free to reach out:
- **GitHub**: [anish-dev21](https://github.com/anish-dev21)

---

Thank you for exploring **hackaTUM-2024**! We hope this repository helps you create impactful solutions.

---

