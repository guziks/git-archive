# Git Archive

Simple utility to archive master commit of git repository.

## System requirements

* java runtime environment 8
* `git` command in the PATH environment variable

## Usage

```
java -jar GitArchive.jar <path to git repository>
```

## Output file

```
<path to git repo>/../<repo folder name>.<yyyyMMdd-HHmm>.<short commit hash>.zip
```

## What's the point

Regular command to create git master commit archive:

```
git archive -o <output file name> master
```

This way you need to specify file name manually. If you like me and often create archives, want them to have pretty names (contain date, time, short hash) it is quite tedious. GitArchive does that for you.