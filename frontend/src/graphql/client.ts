import { ApolloClient, HttpLink, InMemoryCache } from '@apollo/client'

// Relative URL: in dev, Vite proxies /graphql to the Spring Boot app (see vite.config.ts).
export const client = new ApolloClient({
  link: new HttpLink({ uri: '/graphql' }),
  cache: new InMemoryCache(),
})
