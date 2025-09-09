FROM node:18-alpine3.16 AS build-step
RUN apk update && apk upgrade
WORKDIR /src/app
COPY package.json .
RUN npm install
COPY . .
RUN npm run build

FROM nginx:1.21.6-alpine
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build-step /src/app/build /store-ui

