package duy.personalproject.taskmanagementsystem.model.response.project;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ProjectResponse(
  UUID id,
  String name,
  String description,
  UUID ownerId,
  Instant createdAt,
  Instant updatedAt
) {}

