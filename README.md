### TempleOSRS Plugin

> A RuneLite plugin utilizing the [TempleOSRS API](https://templeosrs.com/api_doc.php). <br>

![image](https://user-images.githubusercontent.com/60162255/169199135-328b4fd4-97df-4927-9218-49ea01fe7729.png)
![image](https://user-images.githubusercontent.com/60162255/169199209-d747b75f-173e-4aab-bb7e-9ccc414d3923.png)

### To-Do / Potential Features

> 1. ✅ **COMPLETED:** Implement functionality for sorting each activity panel by name, gain, rank, or EHP.
> 2. ✅ **COMPLETED:** Right-click menu option to lookup player.
> 3. ⚙️ **IN PROGRESS:** Add support for other TempleOSRS API Endpoints. (Competitions, Aliases, Community, Groups)
>     * ⚓ Implemented class skeleton for Groups/ Competitions + scrollable tab groups
> 4. ✅ **COMPLETED:** Save a Snapshot of player's Skills/ Bosses tab.

### Bugs to fix/ Concerns

> 1. ⚠️~~Thread is locked up while fetching player data.~~
>    * **FIXED:** Created new thread to handle data fetching/ panel rebuilding
> 2. 🐛 Searching for players who have recently changed names and have yet to update their TempleOSRS profile will cause issues.
> 3. 📓 Players without data-points on temple return nothing.
>    * **POTENTIAL FIX:** Send request to update player before fetching user.
