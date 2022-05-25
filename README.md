### TempleOSRS Plugin

> A RuneLite plugin utilizing the [TempleOSRS API](https://templeosrs.com/api_doc.php). <br>

![1653501046](https://user-images.githubusercontent.com/60162255/170330279-163c0c28-9ac8-47ea-aa28-4d988a1177dc.png)
![1653501038](https://user-images.githubusercontent.com/60162255/170330283-df86edad-24ed-4b62-8cf2-01592d37fbe5.png)
![1653501035](https://user-images.githubusercontent.com/60162255/170330286-85daace1-1698-43a6-b980-4e619acb265b.png)


### To-Do / Potential Features

> 1. ✅ **COMPLETED:** ~~Implement functionality for sorting each activity panel by name, gain, rank, or EHP.~~
> 2. ✅ **COMPLETED:** ~~Right-click menu option to lookup player.~~
> 3. ⚙️ **IN PROGRESS:** Add support for other TempleOSRS API Endpoints. (Competitions, Groups)
>     * ✅ ~~Implemented class skeleton for Groups/ Competitions~~
>     * ✅ ~~Design *Clans tab* (Fetch clan, Overview, leaders, members, member lookup, members sync)~~
>     * 🔧 Design *Competitions tab* ~~(Fetch competition, competition rankings, participant lookup,~~ add to watchlist)
> 4. ✅ **COMPLETED:** ~~Save a Snapshot of the current view.~~
> 5. 💡 Add toggle to hide skills/bosses without gains.
> 6. ⚠️ Add rigorous comments.

### Bugs to fix/ Concerns

> 1. ⚠️~~Thread is locked up while fetching player data.~~
>    * **FIXED:** Created new thread to handle data fetching/ panel rebuilding
> 2. 🐛 Searching for players who have recently changed names and have yet to update their TempleOSRS profile return nothing.
> 3. 🐛 Players without data-points on temple return nothing.
>    * **POTENTIAL FIX:** Send request to update player before fetching user.
> 4. 📓 Fetching clans with larger member-counts causes a short delay while changing tabs.
>    * **COMPROMISE:** ~~Add togglable display of clan-members.~~
