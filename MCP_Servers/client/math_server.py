from mcp.server.fastmcp import FastMCP

# Server Name
server = FastMCP("Math")

# Tools inside Server
def add(a:int,b:int) ->int:
    """
    Adding two numbers passed in a, b and returning the sum of a,b
    """
    return a+b

def multiply(a:int,b:int) -> int:
    """
    This method will take 2 inputs a and b multiply and return the value
    """
    return a*b

# This server uses Standard Input and Output (Stdio) as Protocol
if __name__ == "__main__":
    server.run(transport="stdio")