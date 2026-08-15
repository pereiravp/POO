# DomusControl

A text-based smart home management system written in Java, inspired by
platforms like Home Assistant. You create houses, split them into rooms, fill
those rooms with connected devices, and then control everything: individually,
on a schedule, or through automations and scenes. A virtual clock lets you
fast-forward time and watch energy consumption add up.

Built as a university group project for Programação Orientada aos Objetos
(Object-Oriented Programming) at Universidade do Minho, then refactored for
portfolio quality. The interface is in Portuguese; the code and this README are
in English.

## Authors

- Gonçalo Pereira ([@pereiravp](https://github.com/pereiravp))
- David Mimoso ([@davidmimoso](https://github.com/davidmimoso))
- Tiago Du ([@Tiago-Du](https://github.com/Tiago-Du))

## What it does

You register users, create houses, split them into rooms, and assign devices
to each room, with owners and access levels for administrators or guests.

Eight device types share a common base (relay, light bulb, speaker, blind,
air conditioner, sprinkler, robot vacuum), each with its own energy draw: a
dimmed bulb uses less, a louder speaker uses more, the vacuum runs off its
own battery. On top of that: automations and scenes that group devices
("Leaving Home", "Movie Night"), schedules, and rules that react to
conditions, like a rain sensor switching the sprinklers off.

A virtual clock drives all of it minute by minute, running schedules,
applying rules, and adding up energy use into statistics and billing. State
saves to and loads from binary object files, so you can close the app and
pick up later where you left off.

## Requirements

A Java Development Kit, version 11 or newer (developed on JDK 21).

## Build and run

```sh
./run.sh            # compile to build/ and start the app
./tests/run_tests.sh   # compile and run the test suite
```

Or by hand:

```sh
mkdir -p build
find src -name "*.java" > sources.txt
javac -d build @sources.txt
java -cp build app.Main
```

On first launch, with no saved data, the app seeds a test state so there's
something to explore. Log in with `admin@uminho.pt` / `poo2026`.

## Architecture

The code is organized in packages by responsibility:

```
src/
  dispositivos/   device hierarchy: Dispositivo (abstract) + 8 concrete types
  model/          domain: Casa, Divisao, Utilizador, Automacao, energy providers
  logic/          Agendamento (scheduled tasks)
  ui/             text menus and input helpers
  persistencia/   saving/loading state (GestorDados)
  app/            Main: wires everything together
tests/            a small no-framework test suite
```

The layers stay separate: domain classes never print to the screen, they just
expose data and leave the UI to decide how to show it. `Main` only
coordinates (loads, hands control to the menu, saves), and the actual file I/O
lives in `persistencia/GestorDados`.

Every domain class follows the same canonical shape expected in Java OOP: a copy
constructor, `equals` and `hashCode` that agree with each other, `clone`, and a
readable `toString`. Copies are deep where it matters: cloning a house clones
its rooms, their devices, its automations and its energy provider, so the copy
is fully independent of the original.

New device types can be added by writing a new subclass of `Dispositivo`,
without touching the classes that already exist.

## Tests

`./tests/run_tests.sh` runs 26 checks with no external dependencies: the
canonical methods behave correctly, copies are genuinely deep, device
consumption is polymorphic, energy bills are computed right, and state survives
a save/load round-trip.

## Notes

- Column labels for devices and IDs are short generated codes; they're meant to
  be unique, not memorable.
- The energy simulation is deliberately simple, enough to produce meaningful
  statistics without pretending to be physically exact.
