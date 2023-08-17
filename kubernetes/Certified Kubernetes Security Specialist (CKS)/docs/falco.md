# Falco



- Falco can detect and alerts on any behavior that involves making Linux system calls

- **Falco** operates at the user space and kernel space, major components...

  - Driver
  - Policy Engine
  - Libraries
  - Falco Rules

- Falco install

- Helm Install Falco as DaemonSet

  ```sh
  helm repo add falcosecurity https://falcosecurity.github.io/charts
  helm repo update
  
  helm upgrade -i falco falcosecurity/falco \
    --set falcosidekick.enabled=true \
    --set falcosidekick.webui.enabled=true \
    --set auditLog.enabled=true \
    --set falco.jsonOutput=true \
    --set falco.fileOutput.enabled=true \
    --set falcosidekick.config.slack.webhookurl="<<web-hook-url>>" 
  ```

  

- **Falco Rules** Filters for engine events, in YAML format Example Rule

  ```yaml
  - rule: Unauthorized process
    desc: There is a running process not described in the base template
    condition: spawned_process and container and k8s.ns.name=ping and not proc.name in (apache2, sh, ping)
    output: Unauthorized process (%proc.cmdline) running in (%container.id)
    priority: ERROR
    tags: [process]
  ```



- **Falco Configuration** for Falco daemon, YAML file and it has key: value or list
  - config file located at `/etc/falco/falco.yaml`
-  https://falco.org/docs/getting-started
-  https://github.com/falcosecurity/charts/tree/master/falco
-  https://falco.org/blog/detect-cve-2020-8557
- https://www.jacobbaek.com/1112





