import os
from agents import run_breakdown_agent, run_restructure_agent

folder_path = 'test_codes'

def file_extraction(root, filename):
    # Full path to the file
    file_path = os.path.join(root, filename)
    # Relative path from the base folder_path
    relative_path = os.path.relpath(file_path, folder_path)

    with open(file_path, 'r') as file:
        content = file.read()

    return relative_path, content

def convert_files_to_yaml(folder_path):
    yaml_files = {}

    for root, _, files in os.walk(folder_path):
        for filename in files:
            # Ensure only relevant files are processed
            if filename.endswith('.java'):
                # Extract relative path and content
                relative_path, content = file_extraction(root, filename)
                # Process content with the agent
                yaml_files[relative_path] = run_breakdown_agent(content)
                # Debugging information
                print(f"Relative Path: {relative_path}")
                print("Generated YAML:")
                print(yaml_files[relative_path])
    
    return yaml_files

def analyse_yaml():
    yaml_files = str(convert_files_to_yaml(folder_path))
    # Restructure the YAML files
    restructured_yaml = run_restructure_agent(yaml_files)
    return restructured_yaml

print(analyse_yaml())


