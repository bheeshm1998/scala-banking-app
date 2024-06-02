# Stage 1: Build the Angular application
FROM node:19 as build

# Set the working directory
WORKDIR /app

# Copy the package.json and package-lock.json to install dependencies
COPY package*.json ./

# Install the dependencies
RUN npm install

# Copy the entire application to the working directory
COPY . .

# Build the Angular application
RUN npm run build --prod

# Stage 2: Serve the Angular application with NGINX
FROM nginx:1.21-alpine

# Copy the build output to the NGINX HTML directory
COPY --from=build /app/dist/ui/browser /usr/share/nginx/html

# Copy the NGINX configuration file
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80 to the outside world
EXPOSE 80

# Start NGINX when the container has started
CMD ["nginx", "-g", "daemon off;"]
