import os

# Specify the directory path
folder_path = '/path/to/your/folder'

# Loop through each file in the directory
for filename in os.listdir(folder_path):
    file_path = os.path.join(folder_path, filename)
    
    # Check if it's a file (not a subdirectory)
    if os.path.isfile(file_path):
        print(f"File: {filename}")
        # You can open or process the file here
        # with open(file_path, 'r') as file:
        #     content = file.read()
        #     print(content)