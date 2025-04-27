# Mautic Helm Chart

This Helm chart deploys Mautic, an open-source marketing automation platform, along with a MariaDB database.

## Prerequisites

- Kubernetes 1.19+
- Helm 3.2.0+
- PV provisioner support in the underlying infrastructure

## Installing the Chart

First, update the dependencies to ensure MariaDB is downloaded:

```bash
helm dependency update ./deploy/mautic
```

Then, install the chart:

```bash
helm install mautic ./deploy/mautic
```

## Configuration

The following table lists the configurable parameters of the Mautic chart and their default values.

| Parameter                  | Description                                     | Default                                                 |
|----------------------------|-------------------------------------------------|---------------------------------------------------------|
| `mariadb.enabled`          | Deploy MariaDB container                        | `true`                                                  |
| `mariadb.auth.database`    | MariaDB database name                           | `mautic`                                                |
| `mariadb.auth.username`    | MariaDB user name                               | `mautic`                                                |
| `mariadb.auth.password`    | MariaDB user password                           | `mautic`                                                |
| `mariadb.auth.rootPassword`| MariaDB root password                           | `mautic`                                                |
| `mariadb.primary.persistence.enabled` | Enable persistence for MariaDB       | `true`                                                  |
| `mariadb.primary.persistence.size`    | PVC size for MariaDB                 | `8Gi`                                                   |

## Accessing Mautic

Once the chart is installed, you can access Mautic through the Ingress host defined in `values.yaml`.

## Troubleshooting

If MariaDB is not being deployed as a pod, ensure that:

1. You've run `helm dependency update` before installing the chart
2. The `mariadb.enabled` parameter is set to `true` in your values.yaml file
3. The Bitnami repository is accessible from your environment