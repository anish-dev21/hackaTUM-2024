import requests

# URL of the API endpoint
url = "http://127.0.0.1:5000/api/refactor"

# JSON payload
payload = {
    "code": "def example():\n    x = 1\n    print(x)"
}

# Send POST request
response = requests.post(url, json=payload)

# Print response from the server
print(response.text)
