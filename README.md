# Welcome to Express Your Emotions (Team 6)!

![alt text](https://github.com/cs160-spring2019/team-project-team6/blob/master/app/src/main/res/drawable-v24/homepage.png "Landing Page of Our App")

## Summary

Studies have shown that children with Autism Spectrum Disorder (ASD) often have difficulty detecting, recognizing, and correlating emotions from facial expressions.
This cognitive impairment tends to worsen with age.
Our app targets at high-functioning autistic children who have the ability to read and comprehend words. 
It aims to provide a platform for children with ASD to learn emotional facial expressions in a fun and engaging way. 
In addition, our app can be used as a educational tool that instructors use to aid these children in learning so.

## Installation

First, download this entire Git repository.

Then, open this application in Android Studio and try to build it. 

Resolve any issues during the build. You may need to follow error messages to install any required dependency or library. Make sure the app is successfully built. 

Finally, run this application on either an Android emulator or a real Android device. 

## Code Structure

Our app consists of 3 major tasks:
    
    1. Learn Your Emotions
    
It shows kids flashcards of different emotional cartoon faces and has them **learn** the definition of each emotion.

    2. Face Matching Game

In this game, the kids will first select an emotion that they want to mimic, and **play** with our app by using the camera to take a selfie of their own face.
The app will then inform them whether they make the right facial expression or not. 

    3. Emotion Quiz

After the kids have a good grasp of different emotions and the corresponding facial expressions, they can **test** their knowledge here. 
    The Emotion Quiz prepares a series of multiple-choice questions, which display cartoon characters and faces.

## Acknowledgement

The majority of images come from https://www.pinclipart.com/, while the rest are from the open Internet. 
Please contact us if they violate any copyrights.

We use Microsoft's Face and Emotion Recognition API to implement Task 2. 
Our app will not store your photos.
And we, as developers, will not be able to see or have access to them.
However, it will send your photos to Microsoft for evaluation. 
For more details about how Microsoft handles your data, refer to https://github.com/microsoft/cognitive-Face-Android, https://azure.microsoft.com/en-us/services/cognitive-services/face/ and its official website.
Please let us know if you believe there is any privacy violation of this API. 
