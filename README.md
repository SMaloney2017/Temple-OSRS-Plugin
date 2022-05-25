### TempleOSRS Plugin

> A RuneLite plugin utilizing the [TempleOSRS API](https://templeosrs.com/api_doc.php). <br>

![1653472988](https://user-images.githubusercontent.com/60162255/170237139-f4888c41-fb16-49b1-9bfd-ddb1f0fab4e8.png)
![1653472981](https://user-images.githubusercontent.com/60162255/170237162-d35b0320-3384-4d58-aec5-4326fdc2d08d.png)
![1653472976](https://user-images.githubusercontent.com/60162255/170237173-9cc8ba95-fbce-42f5-ac89-36436ed8c96d.png)


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
