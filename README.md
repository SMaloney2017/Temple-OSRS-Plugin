### TempleOSRS Plugin

> A RuneLite plugin utilizing the [TempleOSRS API](https://templeosrs.com/api_doc.php). <br>

![1653472362](https://user-images.githubusercontent.com/60162255/170235186-86073de6-dd1f-4abc-b46b-67ce53ae9a4e.png)
![1653472366](https://user-images.githubusercontent.com/60162255/170235201-b15e97d2-8e50-479d-9bc3-efcb28d572c5.png)
![1653472371](https://user-images.githubusercontent.com/60162255/170235209-7ef2dab5-c7fe-442c-b75e-e51b7126b099.png)


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
