### TempleOSRS Plugin

A RuneLite plugin utilizing the [TempleOSRS API](https://templeosrs.com/api_doc.php). <br>
- - -

### Features

1. Fetch player's recorded gains by **username**. <br>
   ![image](https://user-images.githubusercontent.com/60162255/170362329-212ec277-db30-4e3b-b590-babed7ba2d16.png)

2. Fetch player via **menu-option** `Toggleable in config` <br>
   ![image](https://user-images.githubusercontent.com/60162255/172024020-49c2df01-ce6e-47a5-9571-c3dad2a03714.png)

3. Fetch clan by **TempleOSRS Clan-ID**. `Clan ID obtainable via TempleOSRS clan-page URL` <br>
   ![image](https://user-images.githubusercontent.com/60162255/177026002-2a807242-d652-43c6-991b-f3408e987649.png)

4. Fetch competition by **TempleOSRS Comp-ID**. `Competition ID obtainable via TempleOSRS competition-page
   URL`<br>
   ![image](https://user-images.githubusercontent.com/60162255/170364287-95dc2423-add6-4564-ba8e-ea04a201b9c5.png)

- - -

### Configuration

![image](https://user-images.githubusercontent.com/60162255/177015346-ef5187fa-341e-4d2f-8c09-6e993147c5f9.png)

#### Menu Options

1. Toggle `Autocomplete`
2. Toggle `Temple menu-option`
3. Toggle `Fetch-defaults`

#### Rank Options
1. Set `default-player` to be fetched on startup or search-icon double-click
2. Set `default-range`
3. Toggle `auto-update` of local-player on logout

#### Clan Options

1. Toggle display of `clan-achievements`
2. Toggle display of `clan-members`
3. Toggle display of `current-top player rankings`
4. `Default clan ID` loaded on startup or search-icon double-click
5. `Verification key` for clan-sync
6. `Ignore specified ranks` during clan-sync
7. Toggle `only add members` during clan-sync rather than add/remove

#### Competition Options

1. `Default competition` loaded on startup or search-icon double-click
- - -

### Frequently Asked Questions

1. Where can I find my Clan Key?
   *  The group's verification-key is used by *group-admins* to edit a group's leaders, members, and type on TempleOSRS. `A group key is generated when a new group is created on TempleOSRS and is usually only accessible to that group's admins`. Clan-Sync uses the group-key to update the fetched group's members to those in the local-player's current clan.
   
2. Where can I find my Clan or Competition's ID?
   *  Clan and Competition IDs are unique identifiers generated on group or competition creation and are used to fetch relevant information. It's possible that there are many groups or competitions with the same name on TempleOSRS, so it's necessary to fetch imformation by ID rather than by name. `Clan and Competition IDs can be found in the URL of the clan or competition's TempleOSRS page. These IDs can be set as defaults in the plugin's configuartion afterwards`.
