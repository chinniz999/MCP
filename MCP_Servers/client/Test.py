import os
from langchain_openai import ChatOpenAI

os.environ["OPENAI_API_KEY"]=os.getenv("OPENAI_API_KEY")

model=ChatOpenAI(model="gpt-4o")

print(model.invoke("What is the size of cricket ball ?"))