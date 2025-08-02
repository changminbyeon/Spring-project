FROM tomcat:10.1-jdk17-temurin

COPY packages.txt /tmp/packages.txt

RUN apt-get update && \
    apt-get install -y --no-install-recommends $(cat /tmp/packages.txt) &&\
    rm -rf /var/lib/apt/list/*

RUN groupadd -g 1001 devgroup && useradd -u 1001 -g devgroup -m -s /bin/bash devuser

RUN echo "devuser ALL=(ALL) NOPASSWD:ALL" > /etc/sudoers.d/devuser && \
    chmod 0440 /etc/sudoers.d/devuser

COPY entrypoint.sh /usr/local/bin

RUN chmod +x /usr/local/bin/entrypoint.sh

ENTRYPOINT ["entrypoint.sh"]

WORKDIR /app

EXPOSE 8080

CMD ["/bin/bash"]