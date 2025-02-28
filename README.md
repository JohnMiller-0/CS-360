# CS-360
Briefly summarize the requirements and goals of the app you developed. What user needs was this app designed to address?

The goals of the app are to create a user profile that allows a user to enter and monitor their weight to reach a weight loss goal that they can choose to be notified of with an SMS message

What screens and features were necessary to support user needs and produce a user-centered UI for the app? How did your UI designs keep users in mind? Why were your designs successful?

The app interacts with a SQLite DB's to store user information and reads the DB to present various weigh-ins in card views that are intergrated into a recycler-view object to allow scrolling. The view weights screen has a settings button that allows users to opt-in or out of text message notifications. Their also exists a login, new user, weight entry, and setting screen.

How did you approach the process of coding your app? What techniques or strategies did you use? How could those techniques or strategies be applied in the future?

I learned the value of iterative development in this project. Often times my initial strategy did not fulfill my goals for the app so much time was spent in redesign and integrating various coding patterns to achieve a successful codebase.

How did you test to ensure your code was functional? Why is this process important, and what did it reveal?

I tested my code using the android emulator which ensured all app functionality would work as intented in real-time.

Consider the full app design and development process from initial planning to finalization. Where did you have to innovate to overcome a challenge?

The database view was to include an edit and delete button that allows users to edit/remove individual database entries. This was a challenge to implement in a table view for me so I pivoted to a card view in a recycler object which allows the edit/delte buttons to be present and dynamically adapt to the entry they are displayed on.

In what specific component of your mobile app were you particularly successful in demonstrating your knowledge, skills, and experience?

I think the card view integration with a recycler view is a good example of dynamically assigning UI components with user data. This is the part of the project I believe works the best and is a good demonstration of the skills I developed throughout the course.
