### TempleOSRS Plugin

> A RuneLite plugin utilizing the [TempleOSRS API](https://templeosrs.com/api_doc.php). <br>

![image](https://user-images.githubusercontent.com/60162255/168956952-9759ebe4-fc67-47bb-ae84-50be41df0f8c.png)

### To-Do / Potential Features

> 1. ✅ **COMPLETED:** Implement functionality for sorting each activity panel by name, gain, rank, or EHP.
> 2. ✅ **COMPLETED:** Right-click menu option to lookup player.
> 3. 💡 Add support for other TempleOSRS API Endpoints. (Competitions, Aliases, Community, Groups)
> 4. ✅ **COMPLETED:** Save a Snapshot of player's Skills/ Bosses tab.

### Bugs to fix/ Concerns

> 1. ⚠️~~Thread is locked up while fetching player data.~~
>    * **FIXED:** Created new thread to handle data fetching/ panel rebuilding
> 2. 🐛 Searching for players who have recently changed names and have yet to update their TempleOSRS profile will cause issues.
> 3. 📓 Players without data-points on temple return nothing.
>    * **POTENTIAL FIX:** Send request to update player before fetching user.
