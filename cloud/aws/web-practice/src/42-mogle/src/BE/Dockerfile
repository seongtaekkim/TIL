FROM node:18-alpine
WORKDIR /app
COPY package*.json /app/
RUN npm ci
COPY . /app/
RUN npm run build
VOLUME [ "/app" ]
EXPOSE 80
CMD ["npm", "run", "start"]
