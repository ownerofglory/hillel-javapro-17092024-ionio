#!/bin/zsh
echo "Looking for a random meal recipe"

meal_json=$(curl -s --location 'www.themealdb.com/api/json/v1/1/random.php')

echo "$meal_json" | sed 's/\\\//\//g' | tr -d '\000-\031' \
  | jq ".meals.[].strMeal,
  .meals.[].strInstructions"