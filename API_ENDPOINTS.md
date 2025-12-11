# API Endpoints Documentation

## Base URL
`http://localhost:8081`

## Changed Endpoints
> [!WARNING]
> These endpoints have changed request/response formats.

### Create Game
Input now requires `locationId` instead of creating location details inline or passing strings.
```bash
curl -X POST http://localhost:8081/games/ \
  -H "Content-Type: application/json" \
  -d '{
    "gameName": "Monopoly",
    "gameInfo": "Classic board game",
    "locationId": 1,
    "gameFloor": "2nd Floor",
    "numberOfPlayers": 4
  }'
```

### Update Game
```bash
curl -X PUT http://localhost:8081/games/1 \
  -H "Content-Type: application/json" \
  -d '{
    "gameName": "Monopoly Deluxe",
    "gameInfo": "Deluxe edition",
    "locationId": 2,
    "gameFloor": "3rd Floor",
    "numberOfPlayers": 6
  }'
```

### Get All Games
Response now contains nested `location` object.
```bash
curl http://localhost:8081/games/
```

### Get Game By ID
```bash
curl http://localhost:8081/games/1
```

### Get Games By Location (City)
```bash
curl http://localhost:8081/games/locations/NewYork
```

## New Endpoints (Location Management)

### Create Location
```bash
curl -X POST http://localhost:8081/locations \
  -H "Content-Type: application/json" \
  -d '{
    "country": "USA",
    "city": "New York",
    "office": "Manhattan Branch"
  }'
```

### Get All Locations
```bash
curl http://localhost:8081/locations
```

### Get Location By ID
```bash
curl http://localhost:8081/locations/1
```

### Update Location
```bash
curl -X PUT http://localhost:8081/locations/1 \
  -H "Content-Type: application/json" \
  -d '{
    "country": "USA",
    "city": "New York",
    "office": "Brooklyn Branch"
  }'
```

### Delete Location
```bash
curl -X DELETE http://localhost:8081/locations/1
```
