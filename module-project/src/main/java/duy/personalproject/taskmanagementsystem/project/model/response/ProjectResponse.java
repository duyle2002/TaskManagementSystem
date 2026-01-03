package duy.personalproject.taskmanagementsystem.project.model.response;

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

