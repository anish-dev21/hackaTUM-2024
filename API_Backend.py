import logging
import csv
from io import StringIO
from flask import Flask, request, jsonify
from backend import run_breakdown_agent, run_restructure_agent, client

# Configure logging
logging.basicConfig(
    level=logging.DEBUG,                  
    format="%(asctime)s [%(levelname)s] %(message)s",
    handlers=[
        logging.StreamHandler(),
        logging.FileHandler("api_debug.log")
    ]
)

# Initialize Flask app
app = Flask(__name__)
logging.info("Flask app initialized.")

@app.route('/process_java_files', methods=['POST'])
def process_java_files():
    """
    Flask endpoint to process Java files and return refactoring suggestions.
    """
    try:
        files = request.form(files)
        print(files)
    except Exception as e:
        print(str(e))
    try:
        # Parse incoming JSON request
        data = request.json
        # logging.debug(f"Received request data: {data}")

        if not data or 'files' not in data:
            logging.error("Invalid request: Missing 'files' field.")
            return jsonify({"error": "Invalid request. Expecting JSON with a 'files' field."}), 400
        logging.debug("===================REQUEST BODY OK===================")

        # Convert each file to yml
        java_files = data['files']
        yml_java_files = []

        for file in java_files:
            # Run Breakdown Agent
            # breakdown_response = client.run(
            #     agent=breakdown_agent,
            #     messages=[{"role": "user", "content": file['content']}]
            # ).messages[-1]["content"]
            breakdown_response = run_breakdown_agent(file['content'])
            yml_java_files.append(breakdown_response)

        logging.debug("===================BREAKDOWN DONE===================")

        logging.debug(yml_java_files)

        logging.debug("===================CONSTRUCT RESTRUCTURE INPUT===================")
        # construct the input to the second agent
        restructuring_input = {}
        for idx, java_file in enumerate(java_files):
            filePath = java_file['relativePath']
            yml_file = yml_java_files[idx]
            restructuring_input[filePath] = yml_file
        
        logging.debug(restructuring_input)
        logging.debug("===================START RESTRUCTURE===================")
        restructure_response = run_restructure_agent(str(restructuring_input))

        logging.debug("===================RESTRUCTURE DONE===================")
        logging.debug(restructure_response)

        formatted_output = csv_to_dict_list(restructure_response)

        return jsonify(formatted_output)

    except Exception as e:
        logging.error(f"Error occurred: {str(e)}")
        return jsonify({"Error": str(e)}), 500

def csv_to_dict_list(csv_content):
    # Define the headers
    headers = [
        "Class Name", "Type", "Package",
        "Complexity", "Category",
        "Priority", "Issue", "Suggestion", "Impact"
    ]
    
    # Read the CSV content and convert to dictionary
    dict_list = []
    csv_reader = csv.reader(csv_content.splitlines())
    for row in csv_reader:
        if len(row) != len(headers):
            continue  # Skip rows with mismatched columns
        entry = {headers[i]: row[i] for i in range(len(headers))}
        dict_list.append(entry)
    
    return dict_list

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)


# from flask import Flask, request, jsonify
# import json
# import os
# import subprocess

# app = Flask(__name__)

# # Define file paths for the input and output JSON files
# INPUT_FILE = "input.json"
# OUTPUT_FILE = "output.json"

# @app.route('/api/refactor', methods=['POST'])
# def refactor_code():
#     """
#     API Endpoint to receive code, save it to a JSON file, call the swarm script, and return the output.
#     """
#     try:
#         # Parse the JSON input
#         data = request.get_json()
#         if not data or 'code' not in data:
#             return jsonify({"error": "Invalid input. 'code' is required."}), 400

#         code = data['code']

#         # Save the received code into a temporary input JSON file
#         with open(INPUT_FILE, 'w') as infile:
#             json.dump({"code": code}, infile)

#         # Call the swarm script
#         # Assuming the script is an executable Python file: `swarm_script.py`
#         try:
#             subprocess.run(["python", "swarm/swarm_script.py"], check=True)
#         except subprocess.CalledProcessError as e:
#             return jsonify({"error": "Error executing swarm script", "details": str(e)}), 500

#         # Read the resulting output JSON file
#         if not os.path.exists(OUTPUT_FILE):
#             return jsonify({"error": "Output file not found. Swarm script may have failed."}), 500

#         with open(OUTPUT_FILE, 'r') as outfile:
#             suggestions = json.load(outfile)

#         # Format the response to include received code and suggestions
#         response = {
#             "received_code": code,
#             "suggestions": suggestions.get("suggestions", [])
#         }

#         # Return the formatted response to display on the webpage
#         return jsonify(response), 200

#     except Exception as e:
#         return jsonify({"error": f"An unexpected error occurred: {str(e)}"}), 500


# if __name__ == '__main__':
#     # Run the Flask API server
#     app.run(host='0.0.0.0', port=5000, debug=True)
