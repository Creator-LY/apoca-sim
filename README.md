# Apoca-Sim
Apocalyptic Simulation allows you to observe and interact with various species represented as moving shapes on your screen. Dive into a virtual ecosystem where each species interacts with one another, causing changes in their statistics and providing a visual representation of the population dynamics over time.

### Features includes

+ Species Interactions: Witness the dynamic interactions between different species as they move around the screen. Observe how their behaviors, such as predation, competition, and symbiosis, affect their populations and overall ecosystem equilibrium.

+ Day/night, rain and lighting system: Addition effect that impacts the ecosystem. Specie changes behaviour on day/night, plant growth rate change on rain and lighting causes unfortunate death in species. 

+ Statistical Changes: Every interaction within the ecosystem causes changes in the species' statistics. These statistics may include population size, birth rates, death rates, energy levels, and other relevant factors.

+ Population Visualization: Offers a visually appealing representation of the changing population dynamics over time. Watch as the shape representing the species grow or shrink, displaying the relative population sizes and the effects of different interactions. Observe patterns, trends, and potential feedback loops as the ecosystem evolves.

+ Editing Tool: Dynamically add or remove species during the simulation. With a simple interface, you can select from a range of species available and introduce them into the ecosystem. As you add new species, the existing ones will react and adapt to the presence of the newcomers, leading to changes in their behaviors and population dynamics. Likewise, removing species from the ecosystem will have an impact on the remaining species, potentially altering their interactions and population sizes.

- Tools are mustly self explanatory, the add corpse tool: `red` for mutated corpse and `light blue` for unmutated corpse. 

## Demo Images

<img src="/demo/play.png" width=340px /><img src="/demo/play2.png" width=340px />

## Simulation Info

<img src="/demo/food-chain.png" width=340px /><img src="/demo/food-chain2.png" width=340px />
:-
<img src="/demo/pyramid.png" width=340px /><img src="/demo/c&s.png" width=340px />

|**Human Behaviour Model**|**Sub-Human Behaviour Model**|**Zombie Behaviour Model**|
| --- | --- | --- |
| + Move towards resource | + Move towards resource or unmutated corpse | + Move towards Nearest Human/Sub-Human |
| + Take resource | + Take resource | |
| + Become unmutated corpse | + Become mutated corpse | + Become mutated corpse |
| + Convert to zombie | + Convert to zombie | |
| + Convert to sub-human | + Kill zombie on day | + Kill Human or Sub-Human |
| + Breed with other humans | + Eat Human on starvation | + Eat corpse |

<br/>

|**Mob Behaviour Model**|**Plant Behaviour Model**|**Resource Behaviour Model**|
| --- | --- | --- |
| + Move towards plant before anything else | + Grow | + Spawn over time base on population |
| + Become corpse | + Try Kill species on touch | |
| + Eat corpse | | |
| + Kill other species | | |
| + Birth new mob | | |

<br/>

|**Day Effect**|**Night Effect**|**Rain Effect**|
| --- | --- | --- |
| + Plant growth|+ Zombie can kill Sub-Human | + Plant gains strength and energy |
| + Sub-Human gain ability to kill zombie | + Plant lose strength|+ Put out fire |


|**Lightning Effect**|
| --- |
| + Cause death to random unlucky victim |
| + Cause fire event on plant |

## Guidelines to setup

### Prerequisites

- Project is managed by `Maven` with requirement in pom.xml

### Run the APP

- Set up `Maven` in VS code and click run.
