from flask import Flask, request, jsonify
import json
import os
import subprocess

app = Flask(__name__)

# Define file paths for the input and output JSON files
INPUT_FILE = "input.json"
OUTPUT_FILE = "output.json"

@app.route('/api/refactor', methods=['POST'])
def refactor_code():
    """
    API Endpoint to receive code, save it to a JSON file, call the swarm script, and return the output.
    """
    try:
        # Parse the JSON input
        data = request.get_json()
        if not data or 'code' not in data:
            return jsonify({"error": "Invalid input. 'code' is required."}), 400

        code = data['code']

        # Save the received code into a temporary input JSON file
        with open(INPUT_FILE, 'w') as infile:
            json.dump({"code": code}, infile)

        # Call the swarm script
        # Assuming the script is an executable Python file: `swarm_script.py`
        try:
            subprocess.run(["python", "swarm/swarm_script.py"], check=True)
        except subprocess.CalledProcessError as e:
            return jsonify({"error": "Error executing swarm script", "details": str(e)}), 500

        # Read the resulting output JSON file
        if not os.path.exists(OUTPUT_FILE):
            return jsonify({"error": "Output file not found. Swarm script may have failed."}), 500

        with open(OUTPUT_FILE, 'r') as outfile:
            suggestions = json.load(outfile)

        # Format the response to include received code and suggestions
        response = {
            "received_code": code,
            "suggestions": suggestions.get("suggestions", [])
        }

        # Return the formatted response to display on the webpage
        return jsonify(response), 200

    except Exception as e:
        return jsonify({"error": f"An unexpected error occurred: {str(e)}"}), 500


if __name__ == '__main__':
    # Run the Flask API server
    app.run(host='0.0.0.0', port=5000, debug=True)
