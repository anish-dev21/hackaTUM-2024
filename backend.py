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
    instructions="""
    You are a senior software architect specializing in code review and refactoring.  
Analyze the provided Java code and generate refactoring suggestions in the following CSV format:  

Class Name,Type (Class/Interface/Enum),Package,Complexity (Low/Medium/High),Category (Architecture/Design Pattern/Code Quality/Performance),Priority (High/Medium/Low),Issue,Suggestion,Impact  

Guidelines for analysis:  
- Strictly adhere to CSV formatting; ensure that columns are properly aligned with headers and separated by commas.  
- Do not have any Markdown backticks
- Enclose any text containing commas in quotes.  
- Focus on architectural and design improvements.  
- Provide specific, actionable recommendations.  
- Include code examples where relevant in the "Suggestion" field, formatted as plain text.  
- Prioritize suggestions based on their impact.  
- Consider SOLID principles and design patterns.  
- Prioritize the introduction of interfaces where it would make the code more maintainable.  
- Consider the semantics of what a class is trying to represent and adjust structures to be more clear.  
- Take into account how the classes interact with each other.  
- Evaluate code maintainability and extensibility.
    """,
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

