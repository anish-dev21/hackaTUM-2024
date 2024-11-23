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
    instructions="You are part of a program for helping new Software developers improve the way that they write code, in this whole app, you are one of the steps that take place so you get a summary of their code in a yaml format and have to give some useful and concise feedback on how to improve the way that they are structuring their code. For this please do not provide any information of the internal way the program works, the user does not now that its code is just being summarized to yaml, just roleplay that you are a teacher and give them the answer.",
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

