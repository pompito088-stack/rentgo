# Tareas SEO — RentGo

Lista priorizada de mejoras SEO (técnico + contenido) para RentGo.
Las tareas marcadas ✅ ya están implementadas en el proyecto. Las ⏳ están pendientes.

---

## 1. SEO Técnico (on-page)

| # | Tarea | Estado | Dónde |
|---|-------|:------:|-------|
| 1.1 | `<title>` único y descriptivo por página (40–60 caracteres) | ✅ | `fragments/layout.html :: head(titulo)` |
| 1.2 | `<meta name="description">` (130–160 caracteres) | ✅ | `layout.html` (fragment `head`) |
| 1.3 | `<meta name="keywords">` (uso secundario) | ✅ | `layout.html` |
| 1.4 | `<meta name="robots" content="index, follow">` | ✅ | `layout.html` |
| 1.5 | `<link rel="canonical">` por URL | ⏳ | comentado en `layout.html`; usar `th:href="${'/' + #request.requestURI}"` cuando se valide en entorno real |
| 1.6 | Open Graph (`og:title`, `og:description`, `og:type`, `og:locale`, `og:site_name`, `og:image`) | ⏳ parcial | añadir `og:image` cuando exista logo definitivo |
| 1.7 | Twitter Card (`summary_large_image`) | ✅ | `layout.html` |
| 1.8 | `<meta name="theme-color">` (color barra navegador móvil) | ✅ | `layout.html` |
| 1.9 | Atributo `lang="es"` en `<html>` | ✅ | todas las plantillas usan `lang="es"` |
| 1.10 | Favicon y apple-touch-icon | ⏳ | crear `/static/favicon.ico` y enlazar |
| 1.11 | `robots.txt` con sitemap | ⏳ | crear `src/main/resources/static/robots.txt` |
| 1.12 | `sitemap.xml` dinámico (vehículos, sucursales, categorías) | ⏳ | crear endpoint `/sitemap.xml` |
| 1.13 | Datos estructurados JSON-LD `AutoRental` | ✅ | `layout.html` |
| 1.14 | JSON-LD `Vehicle` por ficha de vehículo | ⏳ | añadir en `vehiculos/detalle.html` |
| 1.15 | JSON-LD `BreadcrumbList` | ⏳ | añadir en páginas con breadcrumb |
| 1.16 | `hreflang` para futuras versiones (EN, CA) | ⏳ | cuando exista i18n |

---

## 2. HTML Semántico y Accesibilidad

| # | Tarea | Estado |
|---|-------|:------:|
| 2.1 | Uso de `<header>`, `<main>`, `<nav>`, `<footer>`, `<aside>`, `<article>`, `<section>` | ✅ |
| 2.2 | Jerarquía de encabezados correcta (`h1` único por página, `h2`, `h3`...) | ✅ revisado en index, detalle, formularios |
| 2.3 | `alt` descriptivo en todas las `<img>` | ✅ revisado vehículos; ⏳ revisar `static/img/carnets` |
| 2.4 | `aria-label` en botones-icono y navegación | ✅ ampliado en hamburger, navbars |
| 2.5 | `aria-expanded` y `aria-controls` en toggles | ✅ hamburger admin |
| 2.6 | `aria-current="page"` en link activo del navbar | ⏳ pendiente añadir |
| 2.7 | Labels asociados a inputs (`for`/`id`) | ✅ |
| 2.8 | Contraste mínimo AA (4.5:1) | ✅ paleta teal validada |
| 2.9 | Foco visible en interactivos (`:focus-visible`) | ⏳ añadir outline custom global |
| 2.10 | Imágenes con `loading="lazy"` debajo del *fold* | ⏳ añadir en grids de vehículos |
| 2.11 | `width`/`height` en `<img>` para evitar CLS | ⏳ añadir |

---

## 3. Performance (Core Web Vitals)

| # | Tarea | Estado |
|---|-------|:------:|
| 3.1 | CSS y JS minificados (Bootstrap CDN ya minificado) | ✅ |
| 3.2 | Fuentes con `display=swap` | ✅ Inter ya configurado |
| 3.3 | `preconnect` a CDN críticos | ⏳ añadir `<link rel="preconnect" href="https://cdn.jsdelivr.net">` |
| 3.4 | Imágenes en formato WebP/AVIF | ⏳ optimizar imágenes de `/static/img/vehiculos` |
| 3.5 | Compresión gzip/brotli (Spring Boot config) | ⏳ activar `server.compression.enabled=true` |
| 3.6 | Cache headers para estáticos | ⏳ `spring.web.resources.cache.cachecontrol.max-age=86400` |
| 3.7 | `defer` o `async` en scripts no críticos | ⏳ revisar Bootstrap bundle |

---

## 4. Contenido y UX SEO

| # | Tarea | Estado |
|---|-------|:------:|
| 4.1 | URLs limpias y semánticas (`/vehiculos/detalle/{id}`) | ✅ |
| 4.2 | URLs amigables con slug (ej. `/vehiculos/toyota-corolla-cross`) | ⏳ propuesta futura |
| 4.3 | Breadcrumb visible en páginas internas | ✅ detalle, formulario reserva |
| 4.4 | Página 404 personalizada | ⏳ crear `templates/error/404.html` |
| 4.5 | Política de privacidad y términos legales | ⏳ crear páginas estáticas |
| 4.6 | Microcopy por categoría (texto descriptivo en `categorias/lista`) | ⏳ |
| 4.7 | FAQ con marcado `FAQPage` JSON-LD | ⏳ crear sección "Preguntas frecuentes" |

---

## 5. SEO Local

| # | Tarea | Estado |
|---|-------|:------:|
| 5.1 | JSON-LD `LocalBusiness` por sucursal | ⏳ añadir en detalle de sucursal |
| 5.2 | Datos NAP (nombre, dirección, teléfono) coherentes | ✅ presentes en footer y sucursales |
| 5.3 | Página por sucursal con contenido único | ✅ `sucursales/detalle` (revisar contenido) |
| 5.4 | Mapa embebido (Google Maps / OSM) | ⏳ añadir iframe en detalle de sucursal |

---

## 6. Indexación y monitorización

| # | Tarea | Estado |
|---|-------|:------:|
| 6.1 | Alta en Google Search Console | ⏳ tras despliegue |
| 6.2 | Alta en Bing Webmaster Tools | ⏳ |
| 6.3 | Verificación de sitemap enviado | ⏳ |
| 6.4 | Análisis Lighthouse / PageSpeed Insights mensual | ⏳ |
| 6.5 | Configurar Google Analytics 4 / Plausible | ⏳ |

---

## 7. Próximos sprints sugeridos

1. **Sprint A — Indexabilidad básica**: 1.10, 1.11, 1.12, 4.4
2. **Sprint B — Rich snippets**: 1.14, 1.15, 5.1, 4.7
3. **Sprint C — Performance**: 3.3 → 3.7
4. **Sprint D — Accesibilidad AAA**: 2.6, 2.9, 2.10, 2.11

