FROM nginx:1.16-alpine-perl

# lein do clean, cljsbuild once min
# or # lein clean; lein cljsbuild once min
# copy resources/public into wherever
# tada serve that out of nginx
RUN mkdir -p /usr/share/nginx/html/js/compiled
COPY resources/public/js/compiled/app.js /usr/share/nginx/html/js/compiled/app.js
# get the index.html ready
# cat resources/public/index.html | tr "\n" " " # this is your index page, but we're going to 
# we're going to serve it via this awful mess from https://serverfault.com/questions/196929/reply-with-200-from-nginx-config-without-serving-a-file
# and https://serverfault.com/questions/676680/can-i-use-nginx-environment-variables-within-static-files-that-nginx-serves
# and we'll mount *that* configuration into the container
COPY nginx.conf /etc/nginx/nginx.conf
