# HidePlayers

HidePlayers is a Minecraft Forge mod made for only rendering certain players in Hypixel Housing.

## Installation

Download the latest build from [here](https://github.com/supern64/HidePlayers/releases).

Place the jar file in your `.minecraft/mods` folder.

## Usage

```
(/hp also works for all of these commands)

/hideplayers - Toggle rendering players.
/hideplayers mode [WHITELIST/RADIUS] - Set filtering mode.

- For WHITELIST mode (specific players) 
/hideplayers add - Add a username to the whitelist.
/hideplayers remove - Remove a username from the whitelist.
/hideplayers list - Display the whitelist.

- For RADIUS mode (players in a radius)
/hideplayers radius [radius] - Specify the radius to hide players in blocks
```

## Known Issues
```
- Player models in Essential/other mods that have a player appear disappear when using either mode
  > Fixing it involves using a mixin, which I am NOT going to get into (yet)
```

## Other Stuff Used
- Essential's [multiversion toolkit](https://github.com/EssentialGG/essential-gradle-toolkit) 
- Patcher's [mapping for 1.12.2 to 1.8](https://github.com/Sk1erLLC/Patcher/blob/master/versions/1.12.2-1.8.9.txt)