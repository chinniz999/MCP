from mcp.server.fastmcp import FastMCP


# Weather Server
weather_server = FastMCP("Weather")

#tools
def get_current_weather(location:str) -> str:
    """
    This server used to get weather location
    """
    return "The weather is Hot"


if __name__ =="__main__":
    weather_server.run(transport="streamable-http")
