# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

Here is a link to a more complete UML diagram: https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYHSQ4AAaz5HRgyQyqRgotGMGACClHDCKAAHtCNIziSyTqDcSpyvyoIycSIVKbCkdLjAFJqUMBtfUZegAKK6lTYAiJW3HXKnbLmcoAFicAGZuv1RupgOTxlMfVBvGUVR07uq3R6wvJpeg+gd0BxMEbmeoHUU7ShymgfAgECG8adqyTVKUQFLMlbaR1GQztMba6djKUFBwOHKBdp2-bO2Oaz2++7MgofGBUrDgDvUiOq6vu2ypzO59vdzawcvToCLtmEdDkWoW1hH8C607s9dc2KYwwOUqxPAeu71BAJZoJMIH7CGlB1hGGDlAATE4TgJgMAGjLBUyPFM4GpJB0F4as5acKYXi+P4ATQOw5IwAAMhA0RJAEaQZFkyDmGyv7lNUdRNK0BjqAkaAJlMqr3FMXwvG8HyyTABy-myX7Oj0Uk4TJoFzPorxLGmqzKZg6kgo65qNjAVAakgWgDjS0mzPJSzYnehgrkSa5khSg7SaOXlnpOHIwNyvJWoKwo5kMgESlKsoAOreA4MBVPpCknoFJphhZDZNi2bbuT+xTOlaABygHSBAagYH60J+gGQZoAA-AhILhjxKEwDGmG9FMSaqCmOkZlm5R6HOkJohikwUZWXbZWaeXztai5FZ5TLduUNnIPZKBpQsSywi57zYpK7qJcljj7QZ7yZRtC25VQ4IwAAZhqBhLh5i0lOUSWJPY10KQ12AUuJrWqTl8CdWA0ZOAAjJJfQDUNRkjdA5Q+MEh7QEgABeKDLCZFZ3eOrKQ+55QWOin1svNpIwBucgoNee5EcedNk4UU4urOLqHjaHPFUtREkegNOQ2Z5QsWxkSqB+pnnN+5MlX+2ExbhwH4V8ItQaWmvkW1SHQ2hGEwFh0lkQRfQ66R+vwcTnjeH4gReCg6DMaxvjMBx6SZJgyHMN9zoVNIPpMT69Q+s0LSiao4mIzbetwTNhsPorGmEYeoswXbKdmXxlnlAgns7vuWe62gbmWbTp4sj5YAs2XEEVwF90TlzIXkswLORWE2ck2uQtPRafM3qt1fraT5QcCg3BboeTfES3A9BR35TSLPFKGD348NnWkswD0ALp+1j0aSpKtGzkMMwOhThm70s1UU7tGQnOTHQjAADigGsj7XH+2hgXagwcv4R2jvYQCCdy622TiZCGhQD6aWtjApOWsTL52+s9ZAsQf5JkXtnKue9J7eRgF3RuidK4r2ymvMhFJR5Hm0EKPuFdqETiwSPHe8hxYElrvTLueC1CwlbqTc8nd6EagQN-Sqi5BbKyWjgsAgjVCwkEceNaacgTOkER-WIst5aYKDn+KYkCkxpgqP0UxKAACS0g0xw1QrGKMTxOIOXVvcUC3Qpg6AQKAaUVo7i7C+FYiqox-gwEaBfEBp8obXxNvfRGVjVDmMsYBWx9jHHOKmK4lAATAJBOmF4voPi-F5Nwp8J4ITALhMiWYSijsaKBGwD4KA2BuDwH7IYQRKRfbcWvsAn6lRagNAgVArGzdoIJiqaMKJoZEEn3KJQrxyDpk6TzifAZz1GaZEEQQiusxVlEOHh5XhWV+EUgoagqhcjaFdwYb3GA-c5EcKslw4APCZB8J7AzTpuyrHsy+WI8o2yukyO4c8x6z0rG2PFpop85RBFwH7OJfRCBPwbPkYMlZaS7GawcU4+Bl9IYB3iQ-bFox0l4syUTep1FnYBEsLPYuyQYAACkIA8mkWqAIJSQDSkAf0oxAkqiUmEi0Kx3Qlm9DacARlUA4AQGLlAA5OLZmIThcCRZVyyUyrlQqpVKqKXSGPlo8y9ZjnlAAFYcrQLsyhzlfF6sVdAQ1NjpBHI7Kctu3zyEL0oSIwewV673KYVFJ5gLMXPTeR8jm9c-k4oDavdkwa1GhrCNC6QcVzpcsMJyAqbCyYvPysMKojrKCsgALy31hWfbMrotSWC9NBBqKBAziVTh1OJ3U4yI2RqmECaNswagbTAYsesn4QvNc9VN3C1peqniqbAu1dlOTITih1srKD6ugImmhybF27RzQLCNRaYByzXDWs4pryjsp5II1F6LTVD3Ph2woJLb6mwTE-Bp9KvCyqhu6WAwBsBtMIPERIPSAEBwGcHUO4dI7R2MKneZ16GbcDwMIhWT7T0gHQ1ABQIHkAgHA2gTDc7PlnO+bhwDVoVG7vbvuxAgH8xSIiouM6MoYCDqMJvAgKU2PgpPZCkeb1tiXv4offqLIUYDszOjV6vwCajogGh5jNwjDaBThDTtkYYAAFYMK9uk-29Mcnsyie4HOSAqm8DRVgPZYAKdibkdjTZqAyjMM3MY3hljR72PxS42Znjc8eSOBne8ydFMFNiY0bWq4Unkwmb6IO8oFmlPWeo7ZqxGn5BaaJTprqBmEl9SRsZ4aZnUuKasypzLsBssOac5RFzXzgV4ZZtnOjBagUAds5IkN3COOym4zPELKVo2RcLtFwqE8hWSdK4l8ro0pvpZqz5oiOXHOEuiVfXTRWjMLdRhV5b1W3PqkPBtxrc0hNTpHue7sl6kEmqfM+v8aqYnvrvmSp+QA

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
