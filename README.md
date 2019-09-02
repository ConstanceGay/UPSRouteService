# UPSRouteService

## Overview of the project
Routing software that will be used to run a laser-printed interactive map of the Paul Sabatier University in Toulouse. A camera above the map will capture the start and end points of a journey using [Topcodes](https://www.irit.fr/~Emmanuel.Dubois/Teaching/M2ProIHM_2016/docs/index.html?topcodes/TopCode.html), will use the [OpenRouteService API](https://openrouteservice.org/) to work out the optimal path which will then be projected onto the map itself.

## Handicap and accessibility

- The user will be able to choose his mode of transportation : 
  - On foot
  - Bicycle
  - Wheelchair

- Visually impaired individuals will be able to : 
  - explore the map --> the software telling them where they are along the way
  - navigate --> they will be guided along the path, using the Text-to-speech library [Mary TTS](http://mary.dfki.de/index.html)

4th year INSA Toulouse internship project with IRIT and Le Catalyseur.
