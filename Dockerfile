# Use Node.js LTS (Long Term Support) as base image
FROM node:20-alpine

# Set working directory
WORKDIR /app

# Copy package files first to leverage Docker cache
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Expose the port the app runs on
EXPOSE 4200

# Start the application with host 0.0.0.0 to make it accessible outside the container
CMD ["npm", "start", "--", "--host", "0.0.0.0", "--disable-host-check"]
