# wildfly-elytron-gsoc
Repo containing summary of the past three months of Google Summer of Code 2019.

During the three months long summer, I worked with WildFly Elytron Team of Red Hat JBoss Middleware (JBoss Community) under [Google Summer of Code 2019](https://summerofcode.withgoogle.com/projects/#6324732998189056).

## Project Outline
WildFly Elytron is a new security framework developed by WildFly to provide a single unified security framework across whole of the application server which replaced the pre existing Java Authentication and Authorization Service (JAAS) which used to be the standard Pluggable Authentication Module (PAM) information security framework. 
To extend Elytron’s functionality beyond the existing components, custom security components can be implemented using existing Elytron’s APIs and SPIs for their usage in WildFly Elytron subsystem. Now to extend this functionality of custom development to scripting languages other than Java, Java ScriptEngine is used. 
To showcase the working, one of the components of authorization, RoleDecoder was essentially given this functionality and was implemented in the subsystem.

## Approach towards the milestones
After thorough discussion about the timeline with my mentors [Darran Lofthouse](https://github.com/darranl) and [Farah Juma](https://github.com/fjuma), we started off with the project.
With the help of their regular support and motivation, I was actually able to achieve something I never thought of! Every Tuesday, at 7:30PM IST, we would get together wishing one a good morning and one a good afternoon, they would take my queries, discuss goals for the week and we would laugh out at something at the end.
Below I have listed my progress as per every evaluation.

### Phase 1 [Evaluation 1]
Primary task was to understand the server integration with Elytron and how all of it's components actually work hand in hand.
Even though Elytron promises to cover all the use cases with it's out of the box components, adding custom support truly adds to it's usability to a great extent. To understand the working, I initially went through the [Elytron Documentation](https://docs.jboss.org/author/display/WFLY/WildFly+Elytron+Security) and implemented the [quickstarts](https://github.com/wildfly/quickstart) that has already been created to make newcomers a bit comfortable with the overall working with the requied dev setup. 
During this phase, I compiled the steps to create [custom role decoder in Java](https://medium.com/@guptab3/creating-a-roledecoder-in-elytron-1dcf4094fb47).

### Phase 2 [Evaluation 2]
After first evaluation, I was well acquainted to the overall codebase and then jumped to the most interesting part of the project, the research phase.
The prime objective of the project was to add functionality to run scripts written in JavaScript or other scripting languages in Java using Java ScriptEngine.
So in simple terms I can run JavaScript/Python or any other scripting language in Java!
I compiled my research on how to run JS scripts in Java in a blog post which contains all the basic steps required to achieve such: [Running your JavaScript scripts in Java](https://medium.com/@guptab3/running-your-javascript-python-scripts-in-java-6b44acbafab4).

### Phase 3 [Evaluation 3]
After the super taxing phase 2, I actually entered the final phase, wherein I worked on the implementation on the work done so far which included, creating a separate [ScriptRoleDecoder class](https://github.com/bhaskargupta98/wildfly-elytron-gsoc/blob/master/src/main/java/org/wildfly/security/script/engine/ScriptRoleDecoder.java) with corresponding Project Object Model [pom.xml](https://github.com/bhaskargupta98/wildfly-elytron-gsoc/blob/master/src/main/java/org/wildfly/security/script/engine/pom.xml) file for adding appropriate dependencies.
Then I entered the testing phase which included creating a [test file](https://github.com/bhaskargupta98/wildfly-elytron-gsoc/blob/master/src/test/java/org/wildfly/security/script/engine/ScriptRoleDecoderTest.java) for this very class to end up with production ready code.
To finally implement the ```script-role-decoder```, I created a blog post guiding through the [usage in Elytron subsystem in WildFly](https://medium.com/@guptab3/implementing-scriptroledecoder-f4039adbaa4e).

## Post GSoC
The best part of open-source is lifelong learning! Though we were able to achieve almost all the milestones in the stipulated time with production ready code, I'll continue to work on the project and extend the functionality to other components and research other ways to make it better in times to come.
