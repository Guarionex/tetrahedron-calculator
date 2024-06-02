# Stage 1: Build the React application
FROM node:16 as build
LABEL authors="Karaya"
WORKDIR /app
COPY package.json ./
COPY package-lock.json ./
RUN npm install
COPY . ./
RUN npm run build

# Stage 2: Serve the React application with Nginx
FROM nginx:alpine
LABEL authors="Karaya"
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
