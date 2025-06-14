#!/bin/bash

# .env 파일에서 환경변수 로드
if [ -f ".env" ]; then
    echo "📄 Loading environment variables from .env file..."
    export $(cat .env | grep -v '#' | xargs)
else
    echo "❌ .env file not found! Please create .env file first."
    exit 1
fi

IMAGE_NAME="literature-graphql"
CONTAINER_NAME="literature-graphql-container"

echo "🔨 Building Docker image..."
docker build -f graphql/Dockerfile -t $IMAGE_NAME .

echo "🛑 Stopping existing container..."
docker stop $CONTAINER_NAME 2>/dev/null || true
docker rm $CONTAINER_NAME 2>/dev/null || true

echo "🚀 Starting container..."
docker run -d \
  --name $CONTAINER_NAME \
  -p 8081:8081 \
  -e SPRING_DATASOURCE_URL="$POSTGRES_URL" \
  -e SPRING_DATASOURCE_USERNAME="$POSTGRES_USER" \
  -e SPRING_DATASOURCE_PASSWORD="$POSTGRES_PASSWORD" \
  -e JWT_ACCESS_KEY="$JWT_ACCESS_KEY" \
  -e JWT_ACCESS_TOKEN_EXPIRATION="$JWT_ACCESS_TOKEN_EXPIRATION" \
  -e MAIL_USER="$MAIL_USER" \
  -e MAIL_PASSWORD="$MAIL_PASSWORD" \
  -e SPRING_PROFILES_ACTIVE="prod" \
  $IMAGE_NAME

echo "✅ Done! graphql is running at 8081 port"
echo "📝 View logs: docker logs -f $CONTAINER_NAME"
