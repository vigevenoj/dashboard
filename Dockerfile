# First, compile the application
FROM openjdk:8-alpine AS builder
RUN apk add curl bash
RUN curl https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein > /usr/bin/lein
RUN chmod 755 /usr/bin/lein
ENV LEIN_ROOT=1
RUN lein --version

RUN mkdir /app
WORKDIR /app
COPY project.clj project.clj
COPY src src
RUN lein clean
RUN lein cljsbuild once min

# This image can be used to serve the application
# It requires two environment variables to be set:
# - API_KEY
# - STOPS_LIST
FROM nginx:1.16-alpine-perl
RUN mkdir -p /usr/share/nginx/html/js/compiled
COPY --from=builder /app/resources/public/js/compiled/app.js /usr/share/nginx/html/js/compiled/app.js
# get the index.html ready
# cat resources/public/index.html | tr "\n" " " # this is your index page, but we're going to 
# serve it via this awful mess from https://serverfault.com/questions/196929/reply-with-200-from-nginx-config-without-serving-a-file
# and https://serverfault.com/questions/676680/can-i-use-nginx-environment-variables-within-static-files-that-nginx-serves
# and we'll mount *that* into the container
COPY nginx.conf /etc/nginx/nginx.conf
