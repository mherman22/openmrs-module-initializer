## Domain 'reports'
The **reports** subfolder holds the [Reporting module](https://github.com/openmrs/openmrs-module-reporting)'s YAML report descriptors, in its `reportdescriptors` subfolder. Every file ending in `.yml` under `reportdescriptors`, including in its subfolders, is loaded as a descriptor; `.yaml` files are ignored. Each descriptor is saved as a report definition, with its report designs, by the Reporting module's own `ReportLoader`. For example:

```bash
reports/
  └── reportdescriptors/
        ├── samplePersons.yml
        └── sql/
              └── persons.sql
```

###### Descriptor example:
```yaml
key: "iniz.sample.persons"
uuid: "b7f5a4f4-9d8e-4a5b-8c3e-2f6d1e0a9c11"
name: "Sample Persons Report"
description: "Lists persons and their gender."
parameters:
  - key: "startDate"
    type: "java.util.Date"
    label: "Start Date"
datasets:
  - key: "persons"
    type: "sql"
    config: "sql/persons.sql"
designs:
  - type: "csv"
```
The descriptor format is the Reporting module's; see its documentation for the supported dataset and design types.

This domain is loaded after the metadata domains a report may depend on, such as concepts, encounter types, locations and forms. It requires Reporting 1.21.0 or later.

#### Loading behaviour
* Descriptors are reloaded whenever Initializer loads this domain. No checksums are kept, since a descriptor's Excel templates live in files beside it and are copied in when it is loaded.
* Wildcard exclusions do not apply to this domain, because the Reporting module lists the descriptor files itself. The whole domain can still be excluded.
* No Reporting release provides `ReportLoader.loadReportsFromConfig(boolean)` yet; once one does, Initializer passes it its own throw-on-error setting. With Reporting 1.21.0 to 2.1.0, the first invalid descriptor stops the Reporting module's load, so the descriptors it had not yet saved are skipped. Every descriptor is parsed before any is saved, so one that cannot be parsed skips them all. The error message is logged, and it stops Initializer only when Initializer is configured to stop on errors.

#### The `reporting.loadReportsFromConfigurationAtStartup` global property
From Reporting 1.24.0, leave this global property set to `false` when using this domain. Otherwise the Reporting module also loads the descriptors itself when it starts. Reporting 1.21.0 to 1.23.x has no such property and always loads them when it starts, so with those versions the descriptors are loaded twice at startup when Initializer also loads this domain then.

#### Further examples:
Please look at the test configuration folder for sample import files for all domains, see [here](../api/src/test/resources/testAppDataDir/configuration).
