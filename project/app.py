import streamlit as st
import pandas as pd

# Use your exact Windows file path here
csv_path = r"C:\Users\chira\Downloads\restaurant_final_risk_classification (5).csv"

# Load merged restaurant classification data
df = pd.read_csv(csv_path)

# Risk ordering dictionary for sorting
risk_order = {
    "High Risk": 1,
    "Medium Risk": 2,
    "Low Risk": 3
}

# Map Category to a numeric ranking for sorting
df['Risk_Rank'] = df['Category'].map(risk_order).fillna(99)

# Get unique cities (up to 5)
cities = df['City'].unique()[:5]

st.title("Restaurant Risk Dashboard by Chiranjith Team name -LoneWolf Team no A6 ")

st.write("Select a city to view ranked restaurant risk:")

# Create clickable buttons for cities
selected_city = None
cols = st.columns(len(cities))
for i, city in enumerate(cities):
    if cols[i].button(city):
        selected_city = city

# Default to first city if no button pressed
if selected_city is None:
    selected_city = cities[0]

st.write(f"Showing restaurants for **{selected_city}**")

# Filter and sort by rank
city_df = df[df['City'] == selected_city].copy()
city_df = city_df.sort_values('Risk_Rank')

# Risk color styling
def color_risk(val):
    if 'high' in str(val).lower():
        color = 'red'
    elif 'medium' in str(val).lower():
        color = 'orange'
    elif 'low' in str(val).lower():
        color = 'green'
    else:
        color = 'grey'
    return f'color: {color}; font-weight: bold'

# Show Restaurant and Category only, with color styling
st.dataframe(city_df[['Restaurant', 'Category']].style.map(color_risk, subset=['Category']))
