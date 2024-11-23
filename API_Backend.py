import logging
from flask import Flask, request, jsonify
from backend import run_classes_agent, run_combination_agent, breakdown_agent, dryness_agent, client

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
        # Parse incoming JSON request
        data = request.json
        logging.debug(f"Received request data: {data}")

        if not data or 'files' not in data:
            logging.error("Invalid request: Missing 'files' field.")
            return jsonify({"error": "Invalid request. Expecting JSON with a 'files' field."}), 400

        # Combine code from all files
        java_files = data['files']
        combined_code = "\n".join(file['contents'] for file in java_files)
        logging.debug(f"Combined code: {combined_code}")

        # Run Breakdown Agent
        breakdown_response = client.run(
            agent=breakdown_agent,
            messages=[{"role": "user", "content": combined_code}]
        ).messages[-1]["content"]
        logging.debug(f"Breakdown Agent response: {breakdown_response}")

        # Run Dryness Agent
        dryness_response = client.run(
            agent=dryness_agent,
            messages=[{"role": "user", "content": combined_code}]
        ).messages[-1]["content"]
        logging.debug(f"Dryness Agent response: {dryness_response}")

        # Run Classes Agent
        classes_response = run_classes_agent(breakdown_response)
        logging.debug(f"Classes Agent response: {classes_response}")

        # Combine Responses
        combination_response = run_combination_agent(classes_response, dryness_response)
        logging.debug(f"Final combined response: {combination_response}")

        # Send response back
        result = {
            "breakdown": breakdown_response,
            "dryness": dryness_response,
            "classes": classes_response,
            "combined": combination_response
        }
        logging.info(f"Response to client: {result}")
        return jsonify(result)

    except Exception as e:
        logging.error(f"Error occurred: {str(e)}")
        return jsonify({"Error": str(e)}), 500

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
