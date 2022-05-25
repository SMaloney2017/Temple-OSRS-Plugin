### TempleOSRS Plugin

> A RuneLite plugin utilizing the [TempleOSRS API](https://templeosrs.com/api_doc.php). <br>

![1653471396](https://user-images.githubusercontent.com/60162255/170231869-1d68eab3-7774-4515-b246-6df801789a38.png)
![1653471379](https://user-images.githubusercontent.com/60162255/170231877-a08d9cb3-d2ff-44cb-a957-e0f0d6c9cc4e.png)
![1653471375](https://user-images.githubusercontent.com/60162255/170231883-9a99696b-924a-4ab2-82e6-8b4e14cfd9b6.png)


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
