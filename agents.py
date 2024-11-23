from swarm import Agent, Swarm
from dotenv import load_dotenv

load_dotenv()
client = Swarm()

breakdown_agent = Agent(
    name="Breakdown Agent",
    instructions="Break down this java class into a smaller representation of the class in yaml, focusing on fields and functions. Only provide the yaml file",
)

restructure_agent = Agent(
    name="Classes Agent",
    instructions="Suppose you are a coding teacher, you are gonna receive a yaml file summarization of my code and you should provide me with feedback on how to improve the structure of my code",
)

# Run Breakdown Agent first
def run_breakdown_agent(message):
    breakdown_response = client.run(
        agent=breakdown_agent,
        messages=[{"role": "user", "content": str(message)}]
    ).messages[-1]["content"]
    return breakdown_response

def run_restructure_agent(output_from_breakdown):
    response = client.run(
        agent=restructure_agent,
        messages=[{"role": "user", "content": output_from_breakdown}]
    ).messages[-1]["content"]
    return response


