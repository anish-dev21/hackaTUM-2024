import os
import agents

folder_path = 'test'

def file_extraction():

    file_path = os.path.join(root, filename)
    
    relative_path = os.path.relpath(file_path, folder_path)
    
    with open(file_path, 'r') as file:
        content = file.read()

    return relative_path, content

yaml_text = ""

for root, dirs, files in os.walk(folder_path):
    for filename in files:
        extracted_text = file_extraction(filename)
        yaml_text
        