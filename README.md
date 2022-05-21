### TempleOSRS Plugin

> A RuneLite plugin utilizing the [TempleOSRS API](https://templeosrs.com/api_doc.php). <br>

![1652985288](https://user-images.githubusercontent.com/60162255/169375087-4ebe59c9-9e61-4dc2-a81f-7a34c9637aa6.png)
![1652985302](https://user-images.githubusercontent.com/60162255/169375155-3bf2767d-865a-4c9e-8e8f-52ff9c2e109b.png)


### To-Do / Potential Features

> 1. ✅ **COMPLETED:** Implement functionality for sorting each activity panel by name, gain, rank, or EHP.
> 2. ✅ **COMPLETED:** Right-click menu option to lookup player.
> 3. ⚙️ **IN PROGRESS:** Add support for other TempleOSRS API Endpoints. (Competitions, Groups)
>     * ✅ ~~Implemented class skeleton for Groups/ Competitions~~
>     * 🔧 Design Clan's tab (~~Fetch clan~~, Overview, leaders, members, members lookup, members sync)
>     * **FUTURE:** Design Competitions's tab (Fetch data,Add to watchlist, competition info, members lookup)
> 4. ✅ **COMPLETED:** Save a Snapshot of the current view.
> 5. Add toggle to hide skills/bosses without gains.

### Bugs to fix/ Concerns

> 1. ⚠️~~Thread is locked up while fetching player data.~~
>    * **FIXED:** Created new thread to handle data fetching/ panel rebuilding
> 2. 📓 Searching for players who have recently changed names and have yet to update their TempleOSRS profile will cause issues.
> 3. 📓 Players without data-points on temple return nothing.
>    * **POTENTIAL FIX:** Send request to update player before fetching user.
> 4. 📓 Fetching clans with larger member-counts causes a short delay while changing tabs.
