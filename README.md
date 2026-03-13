# hTreecapitator
Simple plugin that will allow you to chop whole tree at once!

![Tree cut animation](https://cdn.modrinth.com/data/cached_images/493aa01faed3ec515ddd4aaf306a14efd26f21c6.gif)

## 🌟Features

- Support Jobs plugin
- Destroys tools
- Permissions
- Custom enchantement
- Full Configurable
- WorldGuard / GriefPrevention / Towny / Lands regions support!
- **On/Off** player command

## 🚀Commands

<table>
  <thead>
    <tr>
      <th scope="col">Command</th>
      <th scope="col">Permission</th>
      <th scope="col">Info</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>/htc</td>
      <td>htreecapitator.help</td>
      <td>Help</td>
    </tr>
    <tr>
      <td>/htc reload</td>
      <td>htreecapitator.reload</td>
      <td>Reload config.yml</td>
    </tr>
    <tr>
      <td>/htc toggle</td>
      <td>htreecapitator.toggle</td>
      <td>On/Off cut trees</td>
    </tr>
    <tr>
      <td>/htc enchant [player]</td>
      <td>htreecapitator.enchant</td>
      <td>give enchanted book</td>
    </tr>
    <tr>
      <td> </td>
      <td>htreecapitator.updatenotify </td>
      <td>Get update notification</td>
    </tr>
  </tbody>
</table>

## ⭐PlaceholderAPI

<table>
  <thead>
    <tr>
      <th scope="col">Placeholder</th>
      <th scope="col">Info</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>%htreecapitator_toggle% </td>
      <td>Get player tree cutting toggle</td>
    </tr>
    <tr>
      <td>%htreecapitator_raw_toggle%</td>
      <td>Get raw player tree cutting toggle</td>
    </tr>
  </tbody>
</table>

## ⚙️Config
<details>
<summary>config.yml</summary>

```
#-------------------------------------------------------------------------------------------------#
#                                                                                                 #
# ██  ██ ██████ █████▄  ██████ ██████ ▄█████ ▄████▄ █████▄ ██ ██████ ▄████▄ ██████ ▄████▄ █████▄  #
# ██████   ██   ██▄▄██▄ ██▄▄   ██▄▄   ██     ██▄▄██ ██▄▄█▀ ██   ██   ██▄▄██   ██   ██  ██ ██▄▄██▄ #
# ██  ██   ██   ██   ██ ██▄▄▄▄ ██▄▄▄▄ ▀█████ ██  ██ ██     ██   ██   ██  ██   ██   ▀████▀ ██   ██ #
#                                                                                                 #
#-------------------------------------------------------------------------------------------------#

# hTreecapitator by Haily

# github: https://github.com/haiilyyy
# discord: https://discord.com/invite/RZ6VX6XcH2

#---------------------------------------#
#               [Config]                #
#---------------------------------------#

messages:
  permission: '&c&l» &7You do not have permission to use this command!'
  console: '&c&l» &7Only players can use this command!'
  help:
    - '&8[&m------------------------------------------------]'
    - ''
    - ' &8&l» &8/&ahtc reload &8- &7reload plugin!'
    - ' &8&l» &8/&ahtc toggle &8- &7on/off!'
    - ' &8&l» &8/&ahtc enchant [player] &8- &7give enchanted book!'
    - ''
    - '&8[&m------------------------------------------------]'
  reload:
    start: '&e&l» &7Reloading plugin...'
    end: '&a&l» &7Plugin reloaded!'
  toggle:
    enable: '&a&l» &7Enabled cutting down trees!'
    disable: '&c&l» &7Disabled cutting down trees!'
  enchant:
    give: '&a&l» &7You received a &aTreecapitator &7enchanted book!'
    give-other: '&a&l» &7Gave a &aTreecapitator &7enchanted book to &a{player}&7!'

# max blocks to cut at once
max-blocks: 128

# worlds where treecapitator is disabled
blocked-worlds:
  - 'example_world'

# cut blocks automatically go to player inventory
auto-pickup-drops: false

# if true, only players with permission can cut down trees
use-permissions: false
permission: 'htreecapitator.use'

# cut only if the player is using an axe
axe-only: true

# if true, the player must hold shift to cut down trees
shift-mining: false

# if true, mangrove roots will break with logs
mangrove-roots: false

# if true, the axe must have the Treecapitator enchantment
# use /htc enchant to give the enchanted book, then apply it on an anvil
require-enchantment: false

# %htreecapitator_toggle% (PlaceholderAPI)
placeholder:
  toggle-on: '&a&lON'
  toggle-off: '&c&lOFF'
```

</details>

## 📊Stats
![stats](https://bstats.org/signatures/bukkit/htreecapitator.svg)