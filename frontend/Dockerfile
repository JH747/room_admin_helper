## build stage 임시 레이어로 처리. 명시적으로 복사되지 않는 한 다음 단계로 이동하지 않으며 즉 최종 이미지에 미포함. 

# Base image
FROM node:18-alpine AS builder

# Set working directory
WORKDIR /app

# Copy all files from the current directory to the working directory
COPY ./frontend .

# Install dependencies
# RUN npm install
RUN npm ci

# Build the Next.js application
RUN npm run build

## production stage from 기준으로 새로운 컨테이너 레이어 시작. 마지막 레이어가 최종 이미지에 포함. 

# Production image
FROM node:18-alpine AS production

# Set working directory
WORKDIR /app

# Copy built files from the builder stage
COPY --from=builder /app/.next ./.next

# 필요한 파일만 복사 
COPY ./frontend/package.json ./package.json
COPY ./frontend/package-lock.json ./package-lock.json
# COPY node_modules ./node_modules # npm ci 에서 생성될 것 
COPY ./frontend/next.config.mjs ./next.config.mjs

# Set environment variable to indicate production mode
ENV NODE_ENV=production
ENV NEXTAUTH_SECRET=temp-next-secret-key
ENV NEXT_PUBLIC_BE_ADDR=http://localhost:8080
ENV NEXT_PUBLIC_BE_INSIDE_NETWORK_ADDR=http://spring-be:8080
ENV NEXT_PUBLIC_BE_INSIDE_NETWORK_WS_ADDR=ws://spring-be:8080

# production 의존성만 설치
RUN npm ci --omit=dev

# Expose the port the app runs on
EXPOSE 3000

# Start the application
# ENTRYPOINT ["npm", "run", "start"]
